#!/bin/bash

# Test 1: Default parameters with steady requirements
echo "Running Test 1 with steady requirements"
python3 -c "
import pyximport; pyximport.install()
from power_manager import PowerManager

manager = PowerManager()
for _ in range(5):
    power = manager.get_power(1000, 500)
    print(f'Allocated power: {power:.2f}')
" > report_power_test1.txt

# Test 2: Variable requirements
echo "Running Test 2 with variable requirements"
python3 -c "
import pyximport; pyximport.install()
import random
from power_manager import PowerManager

manager = PowerManager()
for i in range(10):
    req_power = 300 + random.random() * 400
    power = manager.get_power(1000, req_power)
    print(f'Requested: {req_power:.2f}, Allocated: {power:.2f}')
" > report_power_test2.txt

# Test 3: Low battery scenario
echo "Running Test 3 with low battery"
python3 -c "
import pyximport; pyximport.install()
from power_manager import PowerManager

manager = PowerManager()
for _ in range(5):
    power = manager.get_power(200, 500)  # Battery can't support full request
    print(f'Allocated power: {power:.2f}')
" > report_power_test3.txt

echo "All tests completed. Reports saved to report_power_test*.txt"
