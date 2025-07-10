#!/bin/bash

# Test 1: Default parameters with clean data
echo "Running Test 1 with default parameters"
python3 -c "
import pyximport; pyximport.install()
import numpy as np
from redundancy_manager import RedundancyManager

manager = RedundancyManager()
primary = np.random.rand(100)
backup1 = primary + np.random.normal(0, 0.01, 100)
backup2 = primary + np.random.normal(0, 0.01, 100)
result = manager.process(primary, backup1, backup2)
print(f'Test 1 - Mean diff: {np.mean(np.abs(result-primary)):.6f}')
" > report_redundancy_test1.txt

# Test 2: High error threshold
echo "Running Test 2 with high error threshold"
python3 -c "
import pyximport; pyximport.install()
import numpy as np
from redundancy_manager import RedundancyManager

manager = RedundancyManager(error_threshold=0.5)
primary = np.random.rand(100)
backup1 = primary + np.random.normal(0, 0.2, 100)
backup2 = primary + np.random.normal(0, 0.2, 100)
result = manager.process(primary, backup1, backup2)
print(f'Test 2 - Mean diff: {np.mean(np.abs(result-primary)):.6f}')
" > report_redundancy_test2.txt

# Test 3: Corrupted backup channels
echo "Running Test 3 with corrupted backup channels"
python3 -c "
import pyximport; pyximport.install()
import numpy as np
from redundancy_manager import RedundancyManager

manager = RedundancyManager()
primary = np.random.rand(100)
backup1 = np.random.rand(100)  # Completely different data
backup2 = np.random.rand(100)  # Completely different data
result = manager.process(primary, backup1, backup2)
print(f'Test 3 - Mean diff: {np.mean(np.abs(result-primary)):.6f}')
" > report_redundancy_test3.txt

echo "All tests completed. Reports saved to report_redundancy_test*.txt"
