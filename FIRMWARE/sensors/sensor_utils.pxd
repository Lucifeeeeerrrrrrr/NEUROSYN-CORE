# Funções auxiliares para sensores
cdef extern from "sensor_c.h":
    int open_sensor(const char*)
    void calibrate_sensor(int)
    void reset_sensor(int)
    double read_sensor_value(int, int)
