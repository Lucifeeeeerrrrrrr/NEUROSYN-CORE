import pytest
from communication.lora import LoRaController
from communication.gsm import GSMController
from communication.protocol_hcode import HCodeProtocol

def test_lora_send_receive():
    lora = LoRaController()
    lora.init()
    assert lora._initialized
    # Simulação: não testa hardware real
    assert isinstance(lora.send_packet(b"test"), int)
    assert isinstance(lora.receive_packet(100), bytes)

def test_gsm_send_receive():
    gsm = GSMController()
    gsm.init()
    assert gsm._initialized
    assert isinstance(gsm.send_sms(b"123", b"msg"), int)
    assert isinstance(gsm.receive_sms(), bytes)

def test_hcode_protocol():
    proto = HCodeProtocol()
    data = {'payload': b'abc'}
    packet = proto.serialize(data)
    assert packet.startswith(b'HCODE')
    out = proto.deserialize(packet)
    assert out['payload'] == b'abc'
