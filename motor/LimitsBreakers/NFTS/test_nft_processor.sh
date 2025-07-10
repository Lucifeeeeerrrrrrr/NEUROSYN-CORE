#!/bin/bash

# Test 1: Basic superposition processing
echo "Running Test 1 with basic input"
python3 -c "
import pyximport; pyximport.install()
import numpy as np
from nft_processor import NFTProcessor

processor = NFTProcessor()
input_data = np.array([1.0, 2.0, 3.0, 4.0, 5.0])
output = processor.process_superposition(input_data)
print(f'Input: {input_data}')
print(f'Output: {output}')
" > report_nft_test1.txt

# Test 2: Weight adaptation
echo "Running Test 2 with weight adaptation"
python3 -c "
import pyximport; pyximport.install()
import numpy as np
from nft_processor import NFTProcessor

processor = NFTProcessor()
input_data = np.array([1.0, 0.5, 0.25])
print('Before adaptation:')
output1 = processor.process_superposition(input_data)
print(f'Output: {output1}')

processor.adapt_weights(np.array([0.1, -0.2, 0.05]))
print('After adaptation:')
output2 = processor.process_superposition(input_data)
print(f'Output: {output2}')
" > report_nft_test2.txt

# Test 3: High chaos mode
echo "Running Test 3 with high chaos"
python3 -c "
import pyximport; pyximport.install()
import numpy as np
from nft_processor import NFTProcessor

processor = NFTProcessor()
for i in range(3):
    input_data = np.random.rand(5)
    output = processor.process_superposition(input_data)
    print(f'Run {i+1} Input: {input_data}')
    print(f'Run {i+1} Output: {output}')
    processor.adapt_weights(np.random.rand(5) - 0.5)
" > report_nft_test3.txt

echo "All tests completed. Reports saved to report_nft_test*.txt"
