# Funções auxiliares para integração Cython/C
cdef extern from "math.h":
    double pow(double, double)
    double exp(double)
    double fabs(double)
