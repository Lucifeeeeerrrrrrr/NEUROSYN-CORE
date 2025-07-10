#!/bin/bash

# Test 1: Default parameters
echo "Running Test 1 with default parameters"
python3 -c "
import pyximport; pyximport.install()
import numpy as np
from bio_cython import BioSensor

sensor = BioSensor()
data = sensor.read_biometric()
print(f'Test 1 - Mean: {np.mean(data):.4f}, Std: {np.std(data):.4f}')
" > report_bio_test1.txt

# Test 2: High sampling rate
echo "Running Test 2 with high sampling rate"
python3 -c "
import pyximport; pyximport.install()
import numpy as np
from bio_cython import BioSensor

sensor = BioSensor(sampling_rate=5000.0)
data = sensor.read_biometric()
print(f'Test 2 - Mean: {np.mean(data):.4f}, Std: {np.std(data):.4f}')
" > report_bio_test2.txt

# Test 3: Low redundancy
echo "Running Test 3 with low redundancy"
python3 -c "
import pyximport; pyximport.install()
import numpy as np
from bio_cython import BioSensor

sensor = BioSensor(redundancy_level=1)
data = sensor.read_biometric()
print(f'Test 3 - Mean: {np.mean(data):.4f}, Std: {np.std(data):.4f}')
" > report_bio_test3.txt

echo "All tests completed. Reports saved to report_bio_test*.txt"
