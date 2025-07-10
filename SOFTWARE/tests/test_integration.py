import numpy as np
from integration.sus_digital_api import query_sus_api
from integration.blockchain_oracle import get_oracle_data
from integration.edge_device_sync import sync_with_edge

# Test data
local_data = np.random.rand(10)
edge_data = np.random.rand(10)

# Test SUS Digital API
def test_sus_api():
    result = query_sus_api(b'https://api.example.com/test', {'param': 'value'})
    assert isinstance(result, dict)

# Test Blockchain Oracle
def test_blockchain_oracle():
    oracle_data = get_oracle_data(b'0x123', b'balance')
    assert 'value' in oracle_data
    assert 'confidence' in oracle_data

# Test Edge Device Sync
def test_edge_sync():
    synced = sync_with_edge(local_data, edge_data)
    assert 'data' in synced
    assert synced['data'].shape == local_data.shape

if __name__ == '__main__':
    test_sus_api()
    test_blockchain_oracle()
    test_edge_sync()
    print("All integration tests passed!")