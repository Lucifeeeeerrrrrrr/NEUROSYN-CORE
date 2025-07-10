#!/bin/bash

# Test 1: Default parameters with normal operation
echo "Running Test 1 with default parameters"
python3 -c "
import pyximport; pyximport.install()
from lora_cython import LoRaDriver

lora = LoRaDriver()
for i in range(5):
    sent = lora.transmit(f'Test message {i}')
    received = lora.receive()
    print(f'Attempt {i+1}: Sent: {sent}, Received: {received}')
" > report_lora_test1.txt

# Test 2: High spreading factor
echo "Running Test 2 with high spreading factor"
python3 -c "
import pyximport; pyximport.install()
from lora_cython import LoRaDriver

lora = LoRaDriver(spreading_factor=12)
for i in range(5):
    sent = lora.transmit(f'Long-range message {i}')
    received = lora.receive()
    print(f'Attempt {i+1}: Sent: {sent}, Received: {received}')
" > report_lora_test2.txt

# Test 3: High chaos factor
echo "Running Test 3 with high chaos factor"
python3 -c "
import pyximport; pyximport.install()
from lora_cython import LoRaDriver

lora = LoRaDriver(chaos_factor=0.5)
for i in range(5):
    sent = lora.transmit(f'Chaotic message {i}')
    received = lora.receive()
    print(f'Attempt {i+1}: Sent: {sent}, Received: {received}')
" > report_lora_test3.txt

echo "All tests completed. Reports saved to report_lora_test*.txt"
