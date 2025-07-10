from distutils.core import setup
from Cython.Build import cythonize
import numpy
setup(
    name='neurosyn_firmware',
    ext_modules=cythonize([
        "sensors/biosensors.pyx",
        "sensors/environmental.pyx",
        "communication/lora.pyx",
        "communication/gsm.pyx",
        "communication/protocol_hcode.pyx",
        "processing/synaptic_pruning.pyx",
        "processing/hebbian_learning.pyx",
        "processing/quantum_emulation.pyx",
        "energy/power_manager.pyx",
        "energy/sleep_manager.pyx",
        "memory/adaptive_memory.pyx",
        "memory/cold_storage.pyx",
        "utils/fault_tolerance.pyx",
        "utils/math_ops.pyx"
    ],
    compiler_directives={
        'language_level': "3",
        'boundscheck': False,
        'wraparound': False
    }),
    include_dirs=[numpy.get_include()]
)
