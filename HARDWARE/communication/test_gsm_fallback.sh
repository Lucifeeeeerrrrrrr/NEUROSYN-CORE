#!/bin/bash

# Test 1: Default parameters with normal operation
echo "Running Test 1 with default parameters"
python3 -c "
import pyximport; pyximport.install()
from gsm_fallback import GSMFallback

gsm = GSMFallback()
for i in range(5):
    sent = gsm.send_message(f'Test message {i}')
    received = gsm.receive_message()
    print(f'Attempt {i+1}: Sent: {sent}, Received: {received}')
" > report_gsm_test1.txt

# Test 2: High chaos factor
echo "Running Test 2 with high chaos factor"
python3 -c "
import pyximport; pyximport.install()
from gsm_fallback import GSMFallback

gsm = GSMFallback(chaos_factor=0.5)
for i in range(5):
    sent = gsm.send_message(f'Chaotic message {i}')
    received = gsm.receive_message()
    print(f'Attempt {i+1}: Sent: {sent}, Received: {received}')
" > report_gsm_test2.txt

# Test 3: Low success rate
echo "Running Test 3 with low success rate"
python3 -c "
import pyximport; pyximport.install()
from gsm_fallback import GSMFallback

gsm = GSMFallback(success_rate=0.3)
for i in range(5):
    sent = gsm.send_message(f'Unreliable message {i}')
    received = gsm.receive_message()
    print(f'Attempt {i+1}: Sent: {sent}, Received: {received}')
" > report_gsm_test3.txt

echo "All tests completed. Reports saved to report_gsm_test*.txt"
