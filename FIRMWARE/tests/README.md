# Testes Automatizados do Firmware NEUROSYN CORE

## Como Executar os Testes

1. Certifique-se de que o ambiente está compilado:
   ```zsh
   cd /home/countz/Downloads/git/NEUROSYN CORE/FIRMWARE
   python setup.py build_ext --inplace
   ```
2. Instale as dependências de teste:
   ```zsh
   pip install pytest pytest-benchmark
   ```
3. Execute todos os testes e gere relatório de desempenho:
   ```zsh
   pytest --benchmark-only --benchmark-autosave --tb=short --maxfail=1 > relatorio_testes.txt
   ```
   Isso irá rodar todos os testes e salvar o relatório de benchmarks automaticamente.

## Como Ler os Relatórios
- O arquivo `relatorio_testes.txt` terá o resumo dos testes, falhas (se houver) e os tempos de execução de cada função crítica.
- Para relatórios detalhados de desempenho, consulte a pasta `.benchmarks` gerada pelo pytest-benchmark.
- Os testes de performance avaliam tempo de resposta de leitura de sensores, processamento, comunicação, memória e utilitários.

## Estrutura dos Testes
- `test_sensors.py`: Testa leitura/calibração de sensores biométricos e ambientais.
- `test_processing.py`: Testa poda sináptica, atualização hebbiana e emulação quântica.
- `test_memory.py`: Testa memória adaptativa e armazenamento frio.
- `test_communication.py`: Testa drivers LoRa, GSM e protocolo H-CODE.
- `test_energy.py`: Testa modos de energia e gerenciamento de sono.
- `test_utils.py`: Testa tolerância a falhas e operações matemáticas auxiliares.

## Interpretação
- **Todos os testes devem passar** (OK) para garantir integridade básica.
- **Benchmarks**: Compare tempos entre execuções para identificar gargalos ou regressões de performance.
- **Falhas**: Se algum teste falhar, o relatório mostrará o erro e a função afetada.
