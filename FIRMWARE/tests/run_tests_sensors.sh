#!/bin/zsh
cd "$(dirname "$0")/.."
source .venv/bin/activate
echo "Rodando testes de sensores 1/3..."
pytest tests/test_sensors.py --benchmark-only --tb=short --maxfail=1 > relatorio_testes_sensors_1.txt
echo "Rodando testes de sensores 2/3..."
pytest tests/test_sensors.py --benchmark-only --tb=short --maxfail=1 > relatorio_testes_sensors_2.txt
echo "Rodando testes de sensores 3/3..."
pytest tests/test_sensors.py --benchmark-only --tb=short --maxfail=1 > relatorio_testes_sensors_3.txt

echo "\nResumo dos resultados:"
for i in 1 2 3; do
  echo "\n--- Execução $i ---"
  tail -n 20 relatorio_testes_sensors_${i}.txt
  grep -q "FAILED" relatorio_testes_sensors_${i}.txt && echo "Falha detectada na execução $i!" || echo "Todos os testes passaram na execução $i."
done