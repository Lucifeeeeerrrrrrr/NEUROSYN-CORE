cdef class GSMController:
    cdef:
        int _initialized
    def __cinit__(self):
        self._initialized = False
    cpdef void init(self):
        self._initialized = gsm_init()
    cpdef int send_sms(self, bytes number, bytes message):
        if not self._initialized:
            self.init()
        return gsm_send_sms(number, message)
    cpdef bytes receive_sms(self):
        return gsm_receive_sms()
