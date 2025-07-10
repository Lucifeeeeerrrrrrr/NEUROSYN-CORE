cdef class PowerManager:
    cdef:
        double _battery_level
        double _solar_input
        int _power_mode  # 0=Normal, 1=Economia, 2=Crítico
    def __cinit__(self):
        self._update_power_status()
    cdef void _update_power_status(self):
        self._battery_level = read_battery_level()
        self._solar_input = read_solar_input()
        if self._battery_level < 20.0 or self._solar_input < 5.0:
            self._power_mode = 2  # Crítico
        elif self._battery_level < 60.0:
            self._power_mode = 1  # Economia
        else:
            self._power_mode = 0  # Normal
    cpdef int get_power_mode(self):
        self._update_power_status()
        return self._power_mode
    cpdef void adjust_performance(self):
        cdef int mode = self.get_power_mode()
        if mode == 2:
            disable_non_essential_peripherals()
            set_cpu_frequency(100)  # MHz
        elif mode == 1:
            set_cpu_frequency(400)
        else:
            set_cpu_frequency(800)
