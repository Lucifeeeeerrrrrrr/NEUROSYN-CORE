cdef class LoRaController:
    cdef:
        int _frequency
        int _sf
        int _bw
        bint _initialized
    def __cinit__(self, int frequency=868, int sf=9, int bw=125):
        self._frequency = frequency
        self._sf = sf
        self._bw = bw
        self._initialized = False
    cpdef void init(self):
        if lora_init(self._frequency, self._sf, self._bw) == 0:
            self._initialized = True
    cpdef int send_packet(self, bytes data):
        if not self._initialized:
            self.init()
        cdef const unsigned char[:] data_view = data
        return lora_send_packet(&data_view[0], len(data))
    cpdef bytes receive_packet(self, int timeout_ms):
        cdef unsigned char buffer[256]
        cdef int length = lora_receive_packet(buffer, 256, timeout_ms)
        if length > 0:
            return bytes(buffer[:length])
        return b""
