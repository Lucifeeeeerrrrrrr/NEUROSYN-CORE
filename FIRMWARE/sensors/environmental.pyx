# distutils: language = c++
# cython: boundscheck=False, wraparound=False
cdef class EnvironmentalSensor:
    cdef:
        int _sensor_fd
        bint _is_calibrated
    def __cinit__(self):
        self._sensor_fd = open_sensor("/dev/i2c-2")  # Exemplo
        self._is_calibrated = False
    cpdef double read_environment(self, int code):
        if not self._is_calibrated:
            self.calibrate()
        return read_sensor_value(self._sensor_fd, code)
    cpdef void calibrate(self):
        calibrate_sensor(self._sensor_fd)
        self._is_calibrated = True
