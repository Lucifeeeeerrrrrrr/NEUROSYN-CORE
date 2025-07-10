import pytest
from energy.power_manager import PowerManager
from energy.sleep_manager import SleepManager

def test_power_manager_modes():
    pm = PowerManager()
    assert pm.get_power_mode() in [0, 1, 2]
    pm.adjust_performance()

def test_sleep_manager():
    sm = SleepManager()
    sm.enter_sleep(1)
    assert sm.get_sleep_mode() == 1
