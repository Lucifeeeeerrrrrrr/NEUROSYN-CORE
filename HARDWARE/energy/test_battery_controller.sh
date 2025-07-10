#!/bin/bash

set -x  # Enable debugging

# Test 1: Default parameters with steady power draw
echo "Running Test 1 with steady power draw"
python3 -c "
print('Starting Python interpreter...')
import sys; print(f'Python path: {sys.path}')
import pyximport; print('Importing pyximport...')
pyximport.install()
print('pyximport installed successfully')

from battery_controller import BatteryController
print('BatteryController imported successfully')

battery = BatteryController()
print('Battery instance created')
for _ in range(5):
    battery.update(100)
    print(f'Level: {battery.get_level():.2f}, Discharge rate: {battery.discharge_rate:.4f}')
" > report_battery_test1.txt 2>&1

# Test 2: Variable power draw
echo "Running Test 2 with variable power draw"
python3 -c "
print('Starting Python interpreter...')
import sys; print(f'Python path: {sys.path}')
import pyximport; print('Importing pyximport...')
pyximport.install()
print('pyximport installed successfully')

import random
from battery_controller import BatteryController
print('BatteryController imported successfully')

battery = BatteryController()
print('Battery instance created')
for i in range(10):
    power = 50 + random.random() * 150
    battery.update(power)
    print(f'Power: {power:.2f}, Level: {battery.get_level():.2f}, Rate: {battery.discharge_rate:.4f}')
" > report_battery_test2.txt 2>&1

# Test 3: High initial discharge
echo "Running Test 3 with high initial discharge"
python3 -c "
print('Starting Python interpreter...')
import sys; print(f'Python path: {sys.path}')
import pyximport; print('Importing pyximport...')
pyximport.install()
print('pyximport installed successfully')

from battery_controller import BatteryController
print('BatteryController imported successfully')

battery = BatteryController()
print('Battery instance created')
battery.update(5000)  # Large initial draw
for _ in range(5):
    battery.update(100)
    print(f'Level: {battery.get_level():.2f}, Discharge rate: {battery.discharge_rate:.4f}')
" > report_battery_test3.txt 2>&1

echo "All tests completed. Reports saved to report_battery_test*.txt"
