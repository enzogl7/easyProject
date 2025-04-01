let graficosRemovidos = [];

function removerGrafico(id) {
    document.getElementById(id).style.display = "none";

    let nomeGrafico = document.querySelector(`#${id} .card-header`).innerText.trim();
    graficosRemovidos.push({ id, nome: nomeGrafico });

    atualizarDropdown();
}

function atualizarDropdown() {
    let dropdown = document.getElementById("restoreList");
    let restoreContainer = document.getElementById("restoreContainer");

    dropdown.innerHTML = "";

    if (graficosRemovidos.length > 0) {
        restoreContainer.style.display = "block";

        graficosRemovidos.forEach((grafico, index) => {
            let item = document.createElement("li");
            let link = document.createElement("a");
            link.classList.add("dropdown-item");
            link.href = "#";
            link.innerText = grafico.nome;
            link.onclick = function () {
                restaurarGrafico(index);
            };

            item.appendChild(link);
            dropdown.appendChild(item);
        });
    } else {
        restoreContainer.style.display = "none";
    }
}

function restaurarGrafico(index) {
    let grafico = graficosRemovidos[index];

    document.getElementById(grafico.id).style.display = "block";
    graficosRemovidos.splice(index, 1);

    atualizarDropdown();
}

fetch("/obterdados")
    .then(response => response.json())
    .then(data => {
        const ctx = document.getElementById("graficoProxFinalizacao").getContext("2d");
        const totalProjetos = data.projetosTotal;

        if (!totalProjetos || totalProjetos.length === 0) {
            console.error("Nenhum projeto encontrado");
            return;
        }

        const hoje = new Date();

        const projetosNoPrazo = totalProjetos.filter(projeto => {
            const previsaoFim = new Date(projeto.previsaoFim);
            return previsaoFim >= hoje;
        }).length;

        const projetosAtrasados = totalProjetos.filter(projeto => {
            const previsaoFim = new Date(projeto.previsaoFim);
            const diferencaDias = (hoje - previsaoFim) / (1000 * 60 * 60 * 24);
            return diferencaDias > 7;
        }).length;

        new Chart(ctx, {
            type: "pie",
            data: {
                labels: ["Projetos Dentro do Prazo", "Projetos Atrasados (+7 dias)"],
                datasets: [{
                    data: [projetosNoPrazo, projetosAtrasados],
                    backgroundColor: ["#36A2EB", "#FF6384"],
                    borderColor: ["#36A2EB", "#FF6384"],
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: "top",
                    }
                }
            }
        });
    })
    .catch(error => console.error("Erro ao carregar os dados: ", error));


fetch("/obterdados")
    .then(response => response.json())
    .then(data => {

        const ctx = document.getElementById("graficoStatus").getContext("2d");
        const totalProjetos = data.projetosTotal;

        if (!totalProjetos || totalProjetos.length === 0) {
            console.error("Nenhum projeto encontrado");
            return;
        }

        const statusCounts = totalProjetos.reduce((acc, projeto) => {
            const status = projeto.status || 'Indefinido';
            acc[status] = (acc[status] || 0) + 1;
            return acc;
        }, {});

        const statusColors = {
            'Pendente': '#ffea63',
            'Em andamento': '#003e65',
            'Concluído': '#578b5a'

        };

        const labels = Object.keys(statusCounts);
        const dataValues = Object.values(statusCounts);
        const backgroundColors = labels.map(status => statusColors[status] || '#C1C1C1');

        new Chart(ctx, {
            type: "bar",
            data: {
                labels: labels,
                datasets: [{
                    label: "Quantidade de Projetos por Status",
                    data: dataValues,
                    backgroundColor: backgroundColors,
                    borderColor: backgroundColors,
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            stepSize: 1
                        }
                    }
                },
                plugins: {
                    legend: {
                        position: "top",
                    }
                }
            }
        });
    })
    .catch(error => console.error("Erro ao carregar os dados: ", error));

fetch("/obterdados")
    .then(response => response.json())
    .then(data => {

        const ctx = document.getElementById("graficoPrioridade").getContext("2d");
        const totalProjetos = data.projetosTotal;

        if (!totalProjetos || totalProjetos.length === 0) {
            console.error("Nenhum projeto encontrado");
            return;
        }

        const prioridadeCounts = totalProjetos.reduce((acc, projeto) => {
            const prioridade = projeto.prioridade || 'Indefinida';
            acc[prioridade] = (acc[prioridade] || 0) + 1;
            return acc;
        }, {});

        const prioridadeColors = {
            'Alta': '#FF6384',
            'Média': '#36A2EB',
            'Baixa': '#C1C1C1'

        };

        const labels = Object.keys(prioridadeCounts);
        const dataValues = Object.values(prioridadeCounts);
        const backgroundColors = labels.map(prioridade => prioridadeColors[prioridade] || '#C1C1C1');

        new Chart(ctx, {
            type: "bar",
            data: {
                labels: labels,
                datasets: [{
                    label: "Quantidade de Projetos por Prioridade",
                    data: dataValues,
                    backgroundColor: backgroundColors,
                    borderColor: backgroundColors,
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            stepSize: 1
                        }
                    }
                },
                plugins: {
                    legend: {
                        position: "top",
                    }
                }
            }
        });
    })
    .catch(error => console.error("Erro ao carregar os dados: ", error));