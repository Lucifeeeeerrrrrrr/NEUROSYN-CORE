import pytest
import time
from memory.adaptive_memory import AdaptiveMemory
from memory.cold_storage import ColdStorage

def test_add_entry_and_decay():
    memory = AdaptiveMemory(0.2)
    memory.add_entry(b"test", 1.0, 0.1)
    assert b"test" in memory.memory_pool
    # Simular passagem de tempo
    memory.memory_pool[b"test"]["timestamp"] -= 86400 * 8  # 8 dias atrás
    memory.apply_daily_decay()
    assert b"test" not in memory.memory_pool  # Deve ser arquivado

def test_cold_storage():
    storage = ColdStorage()
    entry = {"value": 42}
    storage.store(b"id", entry)
    assert storage.retrieve(b"id") == entry
