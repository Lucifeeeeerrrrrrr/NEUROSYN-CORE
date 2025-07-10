import pytest
import numpy as np
from processing.synaptic_pruning import prune_connections, apply_hebbian_decay
from processing.hebbian_learning import hebbian_update
from processing.quantum_emulation import quantum_emulate

def test_prune_connections():
    weights = np.random.rand(10, 10)
    pruned = prune_connections(weights.copy(), 0.5)
    assert (pruned[pruned < 0.5] == 0).all()

def test_hebbian_update():
    weights = np.zeros((5, 5))
    activations = np.ones(5)
    updated = hebbian_update(weights, activations, 0.1)
    assert np.allclose(updated, 0.1)

def test_apply_hebbian_decay():
    weights = np.ones((5, 5))
    decayed = apply_hebbian_decay(weights, 0.1)
    assert (decayed < 1.0).all()

def test_quantum_emulate():
    matrix = np.random.rand(8, 8)
    result = quantum_emulate(matrix)
    assert result.shape == (8, 8)
