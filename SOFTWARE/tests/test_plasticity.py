import numpy as np
from plasticity.hebbian_updater import hebbian_update
from plasticity.manifold_embed import manifold_embed
from plasticity.competitive_plasticity import competitive_update

# Test data
weights = np.random.rand(10, 5)
activations = np.random.rand(10)
input_data = np.random.rand(100, 10)

# Test Hebbian Updater
def test_hebbian_updater():
    updated_weights = hebbian_update(weights, activations, 0.01)
    assert updated_weights.shape == weights.shape
    assert not np.array_equal(updated_weights, weights)

# Test Manifold Embed
def test_manifold_embed():
    embedded = manifold_embed(input_data)
    assert embedded.shape[0] == input_data.shape[0]
    assert embedded.shape[1] <= input_data.shape[1]

# Test Competitive Plasticity
def test_competitive_plasticity():
    updated_weights = competitive_update(weights, activations, 0.01)
    assert updated_weights.shape == weights.shape
    assert np.sum(updated_weights != 0) <= np.sum(weights != 0)

if __name__ == '__main__':
    test_hebbian_updater()
    test_manifold_embed()
    test_competitive_plasticity()
    print("All plasticity tests passed!")