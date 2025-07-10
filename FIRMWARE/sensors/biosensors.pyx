# distutils: language = c++
# cython: boundscheck=False, wraparound=False
cdef class BioSensorDriver:
    cdef:
        int _sensor_fd
        bint _is_calibrated
    def __cinit__(self):
        self._sensor_fd = open_sensor("/dev/i2c-1")  # Função C hypotética
        self._is_calibrated = False
    cpdef double read_biomarker(self, int biomarker_code):
        cdef double value
        if not self._is_calibrated:
            self.calibrate()
        value = read_sensor_value(self._sensor_fd, biomarker_code)
        return value
    cpdef void calibrate(self):
        calibrate_sensor(self._sensor_fd)
        self._is_calibrated = True
    cdef void _reset(self):
        reset_sensor(self._sensor_fd)
        self._is_calibrated = False
