cdef class SleepManager:
    cdef:
        int _sleep_mode
    def __cinit__(self):
        self._sleep_mode = 0
    cpdef void enter_sleep(self, int mode):
        self._sleep_mode = mode
        # Chamada C hipotética: set_sleep_mode(mode)
    cpdef int get_sleep_mode(self):
        return self._sleep_mode
