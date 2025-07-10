#!/bin/bash

# Test 1: Basic quantum-symbolic processing
echo "Running Test 1 with basic input"
python3 -c "
import pyximport; pyximport.install()
import numpy as np
from integrated_nft_processor import IntegratedNFTProcessor

processor = IntegratedNFTProcessor()
input_data = np.array([1.0, 0.5, 0.25, 0.125])
output = processor.process_with_collapse(input_data)
print(f'Input: {input_data}')
print(f'Output: {output}')
print(f'Archetype weights: {processor.archetype_weights}')
" > report_integrated_test1.txt

# Test 2: Archetype adaptation
echo "Running Test 2 with archetype adaptation"
python3 -c "
import pyximport; pyximport.install()
import numpy as np
from integrated_nft_processor import IntegratedNFTProcessor

processor = IntegratedNFTProcessor()
for i in range(3):
    input_data = np.random.rand(4)
    output = processor.process_with_collapse(input_data)
    print(f'Run {i+1} Input: {input_data}')
    print(f'Run {i+1} Output: {output}')
    processor.adapt_archetypes(np.random.rand(4) - 0.5)
    print(f'Run {i+1} Weights: {processor.archetype_weights}')
" > report_integrated_test2.txt

echo "All tests completed. Reports saved to report_integrated_test*.txt"
