import time
import random
from libc.math cimport exp, sqrt, log

# From LCM.md and Shadow archetypes
DEF QUANTUM_STATES = 150
DEF CHAOS_FACTOR = 0.05

cdef class AdaptiveMemory:
    cdef:
        dict memory_pool
        double _relevance_threshold
        double[QUANTUM_STATES] state_weights
    
    def __cinit__(self, double relevance_threshold=0.2):
        """Initialize with quantum state patterns from NFT processor"""
        self.memory_pool = {}
        self._relevance_threshold = relevance_threshold
        
        # Initialize quantum state weights
        cdef int i
        for i in range(QUANTUM_STATES):
            self.state_weights[i] = 1.0/QUANTUM_STATES
    
    cpdef void add_entry(self, bytes entry_id, double value, double relevance):
        """Add memory entry with quantum state influence"""
        # Apply quantum influence to relevance
        cdef double quantum_influence = 0.0
        cdef int i
        for i in range(QUANTUM_STATES):
            quantum_influence += self.state_weights[i] * (random.random() * 0.1 - 0.05)
        
        self.memory_pool[entry_id] = {
            'value': value * (1.0 + quantum_influence),
            'relevance': relevance * (1.0 + quantum_influence),
            'timestamp': time.time(),
            'decay_counter': 0,
            'quantum_state': random.randint(0, QUANTUM_STATES-1)
        }
    
    cpdef void apply_daily_decay(self):
        """Apply decay with NFT quantum patterns and Shadow archetype resilience"""
        cdef double current_time = time.time()
        cdef double seconds_per_day = 86400.0
        
        for entry_id, entry in self.memory_pool.items():
            # Shadow archetype: Chaotic but bounded decay
            cdef double chaos = CHAOS_FACTOR * random.random()
            cdef double decay_factor = 1.0 - (entry['relevance'] * (0.7 + chaos))
            
            # Quantum state influence on decay
            cdef double quantum_influence = self.state_weights[entry['quantum_state']] * 0.1
            decay_factor *= (1.0 + quantum_influence)
            
            cdef double elapsed_days = (current_time - entry['timestamp']) / seconds_per_day
            entry['value'] *= pow(decay_factor, elapsed_days)
            
            if entry['value'] < self._relevance_threshold * (0.9 + chaos):
                entry['decay_counter'] += elapsed_days
                
            # Ghost Protocol: Random resilience boost
            if random.random() < 0.05:
                entry['value'] *= 1.1
                
            if entry['decay_counter'] > (7.0 * (1.0 + chaos)):
                self.archive_entry(entry_id)
    
    cpdef void archive_entry(self, bytes entry_id):
        """Archive entry with multi-level validation (LCM.md)"""
        # Level 1 validation: Basic threshold
        if self.memory_pool[entry_id]['value'] < self._relevance_threshold * 0.5:
            save_to_cold_storage(entry_id, self.memory_pool[entry_id])
            del self.memory_pool[entry_id]
