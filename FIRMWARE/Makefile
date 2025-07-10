all: biosensors.c environmental.c lora.c gsm.c protocol_hcode.c synaptic_pruning.c hebbian_learning.c quantum_emulation.c power_manager.c sleep_manager.c adaptive_memory.c cold_storage.c
%.c: %.pyx
	cython -3 --embed $< -o $@
build:
	python setup.py build_ext --inplace
clean:
	rm -f *.c *.so
