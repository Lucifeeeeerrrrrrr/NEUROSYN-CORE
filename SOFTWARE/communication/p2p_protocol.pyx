import numpy as np
cimport numpy as cnp
from libc.math cimport exp, sqrt, log
import random

# From LCM.md and Shadow archetypes
DEF QUANTUM_STATES = 150
DEF CHAOS_FACTOR = 0.05
DEF TRICKSTER_INFLUENCE = 0.1

cdef class P2PProtocol:
    cdef:
        dict _nodes
        double[QUANTUM_STATES] state_weights
    
    def __cinit__(self):
        """Initialize with quantum state patterns"""
        self._nodes = {}
        
        # Initialize quantum state weights
        cdef int i
        for i in range(QUANTUM_STATES):
            self.state_weights[i] = 1.0/QUANTUM_STATES
    
    cpdef void add_node(self, bytes node_id, object node_info):
        """Add node with Shadow archetype resilience"""
        # Quantum state influence
        cdef double quantum_influence = 0.0
        cdef int i
        for i in range(QUANTUM_STATES):
            quantum_influence += self.state_weights[i] * (random.random() * 0.1 - 0.05)
        
        # Trickster archetype: Random node mutation
        if random.random() < TRICKSTER_INFLUENCE:
            if 'latency' in node_info:
                node_info['latency'] *= (0.8 + 0.4 * random.random())
        
        # Ghost Protocol: Chaotic but bounded node addition
        self._nodes[node_id] = {
            'info': node_info,
            'quantum_state': random.randint(0, QUANTUM_STATES-1),
            'noise_factor': CHAOS_FACTOR * random.random()
        }
    
    cpdef bytes select_peer(self):
        """Select peer with quantum-influenced decision making"""
        if not self._nodes:
            return b''
            
        # Quantum state influence on selection
        cdef double[QUANTUM_STATES] influences
        cdef int i
        for i in range(QUANTUM_STATES):
            influences[i] = self.state_weights[i] * random.random()
        
        # Shadow archetype: Multi-level validation
        cdef double max_influence = max(influences)
        cdef list candidates = [
            node_id for node_id, node in self._nodes.items() 
            if node['quantum_state'] == influences.argmax()
        ]
        
        # Trickster archetype: Random selection override
        if random.random() < TRICKSTER_INFLUENCE:
            return random.choice(list(self._nodes.keys()))
            
        return random.choice(candidates) if candidates else random.choice(list(self._nodes.keys()))