cdef class HCodeProtocol:
    cdef:
        int _packet_size
    def __cinit__(self, int packet_size=128):
        self._packet_size = packet_size
    cpdef bytes serialize(self, dict data):
        # Exemplo simplificado
        return b"HCODE" + data.get('payload', b"")
    cpdef dict deserialize(self, bytes packet):
        # Exemplo simplificado
        return {'payload': packet[5:]}
