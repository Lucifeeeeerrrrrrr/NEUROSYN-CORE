#!/bin/bash

# Test 1: Basic validation
echo "Running Test 1 with basic validation"
python3 -c "
import pyximport; pyximport.install()
import numpy as np
from nft_validator import NFTValidator

validator = NFTValidator()
data = np.array([0.85, 0.9, 0.88])
for level in range(3):
    result = validator.validate(data, level)
    print(f'Level {level} validation: {result}')
" > report_validator_test1.txt

# Test 2: Edge cases
echo "Running Test 2 with edge cases"
python3 -c "
import pyximport; pyximport.install()
import numpy as np
from nft_validator import NFTValidator

validator = NFTValidator()
for i in range(5):
    data = np.random.rand(3) * 0.5 + 0.5  # Values between 0.5-1.0
    result = validator.validate(data, 1)
    print(f'Test {i+1}: Data: {data}, Valid: {result}')
" > report_validator_test2.txt

# Test 3: Threshold adaptation
echo "Running Test 3 with threshold adaptation"
python3 -c "
import pyximport; pyximport.install()
import numpy as np
from nft_validator import NFTValidator

validator = NFTValidator()
print('Before adaptation:')
data = np.array([0.82, 0.83, 0.84])
print(f'Validation: {validator.validate(data, 0)}')

validator.adapt_thresholds(np.array([0.1, -0.1, 0.05]))
print('After adaptation:')
print(f'Validation: {validator.validate(data, 0)}')
" > report_validator_test3.txt

echo "All tests completed. Reports saved to report_validator_test*.txt"
