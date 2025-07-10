#!/bin/bash

# Test 1: Default parameters with clean message
echo "Running Test 1 with default parameters"
python3 -c "
import pyximport; pyximport.install()
from p2p_handler import P2PHandler

handler = P2PHandler()
message = 'Test message for Ghost Protocol'
encoded = handler.encode(message)
decoded = handler.decode(encoded)
print(f'Test 1 - Original: {message}')
print(f'Test 1 - Decoded: {decoded}')
" > report_p2p_test1.txt

# Test 2: High chaos factor
echo "Running Test 2 with high chaos factor"
python3 -c "
import pyximport; pyximport.install()
from p2p_handler import P2PHandler

handler = P2PHandler(chaos_factor=0.5)
message = 'Chaotic message with high noise'
encoded = handler.encode(message)
decoded = handler.decode(encoded)
print(f'Test 2 - Original: {message}')
print(f'Test 2 - Decoded: {decoded}')
" > report_p2p_test2.txt

# Test 3: Corrupted message recovery
echo "Running Test 3 with corrupted message"
python3 -c "
import pyximport; pyximport.install()
from p2p_handler import P2PHandler

handler = P2PHandler()
corrupted = bytes([255, 200, 150, 100, 50])  # Invalid UTF-8
decoded = handler.decode(corrupted)
print(f'Test 3 - Corrupted input: {corrupted}')
print(f'Test 3 - Decoded: {decoded}')
" > report_p2p_test3.txt

echo "All tests completed. Reports saved to report_p2p_test*.txt"
