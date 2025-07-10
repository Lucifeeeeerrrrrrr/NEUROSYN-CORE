import pytest
from utils.fault_tolerance import FaultTolerance
from utils.math_ops import safe_divide

def test_fault_tolerance():
    ft = FaultTolerance()
    assert ft.check_integrity([1, 1, 2])
    assert not ft.check_integrity([1, 2])

def test_safe_divide():
    assert safe_divide(10, 2) == 5.0
    assert safe_divide(10, 0) == 0.0
