#!/usr/bin/env python3
import sys
import pyximport
print("Python path:", sys.path)

try:
    print("Attempting to install pyximport...")
    pyximport.install()
    print("pyximport installed successfully")
    
    print("Importing BatteryController...")
    from battery_controller import BatteryController
    print("Import successful")
    
    print("Creating instance...")
    bc = BatteryController()
    print("Instance created successfully")
    
    print("Testing methods...")
    print("Initial level:", bc.get_level())
    bc.update(100)
    print("After update:", bc.get_level())
    
except Exception as e:
    print("ERROR:", str(e), file=sys.stderr)
    raise
