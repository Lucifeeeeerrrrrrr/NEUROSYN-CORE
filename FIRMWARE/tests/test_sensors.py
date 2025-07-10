import pytest
import time
from sensors.biosensors import BioSensorDriver
from sensors.environmental import EnvironmentalSensor

def test_biosensor_read():
    sensor = BioSensorDriver()
    value = sensor.read_biomarker(0x55)
    assert isinstance(value, float)

def test_environmental_read():
    sensor = EnvironmentalSensor()
    value = sensor.read_environment(0x01)
    assert isinstance(value, float)

def test_biosensor_calibration():
    sensor = BioSensorDriver()
    sensor.calibrate()
    assert sensor._is_calibrated

def test_environmental_calibration():
    sensor = EnvironmentalSensor()
    sensor.calibrate()
    assert sensor._is_calibrated

def test_biosensor_performance(benchmark):
    sensor = BioSensorDriver()
    result = benchmark(sensor.read_biomarker, 0x55)
    assert isinstance(result, float)

def test_environmental_performance(benchmark):
    sensor = EnvironmentalSensor()
    result = benchmark(sensor.read_environment, 0x01)
    assert isinstance(result, float)
