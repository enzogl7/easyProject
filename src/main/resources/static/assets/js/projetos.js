$(document).ready(function () {
    paginacaoTabela('tabelaProjetos')
});

document.addEventListener("DOMContentLoaded", function () {
    var hoje = new Date().toISOString().split('T')[0];
    document.getElementById("dataFim").value = hoje;
    console.log("RODOU")

    const checkboxIniciado = document.getElementById("iniciado");
    const inputDataInicio = document.getElementById("dataInicio");
    const textDataInicio = document.getElementById("text-data-inicio")

    inputDataInicio.disabled = true;
    textDataInicio.hidden = false;

    checkboxIniciado.addEventListener("change", function () {
        if (this.checked) {
            inputDataInicio.disabled = false;
            inputDataInicio.value = hoje;
            textDataInicio.hidden = true;
        } else {
            inputDataInicio.disabled = true;
            inputDataInicio.value = "";
            textDataInicio.hidden = false;
        }
    });
});

document.getElementById("nome").addEventListener("keyup", filtrarTabela);
document.getElementById("statusBusca").addEventListener("change", filtrarTabela);
document.getElementById("prioridadeBusca").addEventListener("change", filtrarTabela);
document.getElementById("dataInicioBusca").addEventListener("change", filtrarTabela);
document.getElementById("dataFimBusca").addEventListener("change", filtrarTabela);
document.getElementById("clienteBusca").addEventListener("change", filtrarTabela);
document.getElementById("responsavelBusca").addEventListener("change", filtrarTabela);
document.getElementById("iniciadoBusca").addEventListener("change", filtrarTabela);

document.getElementById("limparFiltros").addEventListener("click", function() {
    document.getElementById("nome").value = "";
    document.getElementById("statusBusca").value = "";
    document.getElementById("prioridadeBusca").value = "";
    document.getElementById("dataInicioBusca").value = "";
    document.getElementById("dataFimBusca").value = "";
    document.getElementById("clienteBusca").value = "";
    document.getElementById("responsavelBusca").value = "";
    document.getElementById("iniciadoBusca").value = "";

    filtrarTabela();
});

function criarProjeto() {
    var formData = new FormData();
    formData.append("nomeProjeto", document.getElementById('nomeProjeto').value);
    formData.append("descricaoProjeto", document.getElementById('descricaoProjeto').value);
    formData.append("dataInicio", document.getElementById('dataInicio').value);
    formData.append("dataFim", document.getElementById('dataFim').value);
    formData.append("solicitanteProjeto", document.getElementById('solicitanteProjeto').value);
    formData.append("cliente", document.getElementById('cliente').value);
    formData.append("responsavel", document.getElementById('responsavelProjeto').value);
    formData.append("status", document.getElementById('status').value);
    formData.append("prioridade", document.getElementById('prioridade').value);
    formData.append("iniciado", document.getElementById('iniciado').checked);

    var anexos = document.getElementById('anexos').files;
    for (var i = 0; i < anexos.length; i++) {
        formData.append("anexos", anexos[i]);
    }

    if (!formData.get("nomeProjeto") || !formData.get("descricaoProjeto") || !formData.get("dataFim") || !formData.get("status") || !formData.get("prioridade") || !formData.get("solicitanteProjeto")) {
        Swal.fire({
            title: "Ops!",
            text: "Preencha todos os campos obrigatórios!",
            icon: "warning",
            confirmButtonText: 'OK'
        });
        return;
    }

    if (formData.get("iniciado") === "true" && !formData.get("dataInicio")) {
        Swal.fire({
            title: "Ops!",
            text: "Preencha a data de início!",
            icon: "warning",
            confirmButtonText: 'OK'
        });
        return;
    }

    $.ajax({
        url: '/projetos/criar',
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        success: function () {
            Swal.fire({
                title: "Sucesso!",
                text: "Projeto criado com sucesso.",
                icon: "success",
                confirmButtonText: "OK"
            }).then(() => {
                window.location.href = "/projetos/lista";
            });
        },
        error: function (xhr, status, error) {
            Swal.fire({
                title: "Ops!",
                text: "Ocorreu um erro ao criar o projeto.",
                icon: "error",
                confirmButtonText: "OK"
            });
        }
    });
}

function iniciarProjeto(button) {
    var idProjeto = button.getAttribute('data-id');

    Swal.fire({
        icon: 'info',
        title: 'Deseja iniciar este projeto?',
        showDenyButton: true,
        confirmButtonText: 'Sim',
        denyButtonText: 'Não',
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: '/projetos/iniciar',
                type: 'POST',
                data: {
                    idProjeto: idProjeto
                },
                success: function() {
                    Swal.fire({
                        title: "Sucesso!",
                        text: "Projeto iniciado com sucesso.",
                        icon: "success",
                        confirmButtonText: "OK"
                    }).then(() => {
                        location.reload();
                    });
                },
                error: function(xhr, status, error) {
                    Swal.fire({
                        title: "Ops!",
                        text: "Ocorreu um erro ao iniciar projeto.",
                        icon: "error",
                        confirmButtonText: "OK"
                    });
                }
            });
        }
    })
}

function excluirProjeto(button) {
    var idProjeto = button.getAttribute('data-id');

    Swal.fire({
        icon: 'info',
        title: 'Deseja excluir este projeto?',
        showDenyButton: true,
        confirmButtonText: 'Sim',
        denyButtonText: 'Não',
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: '/projetos/excluir',
                type: 'POST',
                data: {
                    idProjeto: idProjeto
                },
                success: function() {
                    Swal.fire({
                        title: "Sucesso!",
                        text: "Projeto excluído com sucesso.",
                        icon: "success",
                        confirmButtonText: "OK"
                    }).then(() => {
                        location.reload();
                    });
                },
                error: function(xhr, status, error) {
                    Swal.fire({
                        title: "Ops!",
                        text: "Ocorreu um erro ao excluir este projeto.",
                        icon: "error",
                        confirmButtonText: "OK"
                    });
                }
            });
        }
    })
}

function formatarData(inputData) {
    if (!inputData) return "";

    var partes = inputData.split("-");
    if (partes.length === 3) {
        return partes[2] + "/" + partes[1] + "/" + partes[0];
    }
    return inputData;
}

function filtrarTabela() {
    var nomeFilter = document.getElementById("nome").value.toLowerCase();
    var statusFilter = document.getElementById("statusBusca").value.toLowerCase();
    var prioridadeFilter = document.getElementById("prioridadeBusca").value.toLowerCase();
    var clienteFilter = document.getElementById("clienteBusca").value.toLowerCase();
    var responsavelFilter = document.getElementById("responsavelBusca").value.toLowerCase();
    var iniciadoFilter = document.getElementById("iniciadoBusca").value.toLowerCase();
    var dataInicioFilter = formatarData(document.getElementById("dataInicioBusca").value);
    var dataFimFilter = formatarData(document.getElementById("dataFimBusca").value);

    var table = document.querySelector(".table");
    var rows = table.querySelectorAll("tbody tr");

    rows.forEach(function(row) {
        var iniciado = row.querySelector("td:nth-child(1)").textContent.toLowerCase();
        var nome = row.querySelector("td:nth-child(2)").textContent.toLowerCase();
        var status = row.querySelector("td:nth-child(4)").textContent.toLowerCase();
        var dataInicio = row.querySelector("td:nth-child(5)").textContent.toLowerCase();
        var dataFim = row.querySelector("td:nth-child(6)").textContent.toLowerCase();
        var cliente = row.querySelector("td:nth-child(7)").textContent.toLowerCase();
        var responsavel = row.querySelector("td:nth-child(8)").textContent.toLowerCase();
        var prioridade = row.querySelector("td:nth-child(9)").textContent.toLowerCase();


        var iniciadoMatch = iniciado.indexOf(iniciadoFilter) > -1 || iniciadoFilter === "";
        var nomeMatch = nome.indexOf(nomeFilter) > -1 || nomeFilter === "";
        var statusMatch = status.indexOf(statusFilter) > -1 || statusFilter === "";
        var dataInicioMatch = dataInicio.indexOf(dataInicioFilter) > -1 || dataInicioFilter === "";
        var dataFimMatch = dataFim.indexOf(dataFimFilter) > -1 || dataFimFilter === "";
        var clienteMatch = cliente.indexOf(clienteFilter) > -1 || clienteFilter === "";
        var responsavelMatch = responsavel.indexOf(responsavelFilter) > -1 || responsavelFilter === "";
        var prioridadeMatch = prioridade.indexOf(prioridadeFilter) > -1 || prioridadeFilter === "";


        if (iniciadoMatch && nomeMatch && statusMatch && dataInicioMatch && dataFimMatch && responsavelMatch && clienteMatch && prioridadeMatch) {
            row.style.display = "";
        } else {
            row.style.display = "none";
        }
    });
}

function modalEditarProjeto(button) {
    var hoje = new Date().toISOString().split('T')[0];

    var idProjeto = button.getAttribute('data-id');
    var iniciado = button.getAttribute('data-iniciado');
    var nome = button.getAttribute('data-nome');
    var descricao = button.getAttribute('data-descricao');
    var status = button.getAttribute('data-status');
    var dataInicio = button.getAttribute('data-inicio');
    var previsaoFim = button.getAttribute('data-fim');
    var clienteEdicaoProjeto = button.getAttribute('data-cliente');
    var responsavel = button.getAttribute('data-responsavel');
    var prioridade = button.getAttribute('data-prioridade');
    $('#modalEditarProjeto').modal('show');

    document.getElementById('idProjetoEdicao').value = idProjeto;
    document.getElementById('nomeProjetoEdicao').value = nome;
    document.getElementById('descricaoProjetoEdicao').value = descricao;
    document.getElementById('statusEdicaoProjeto').value = status;
    document.getElementById('prioridadeEdicao').value = prioridade;
    document.getElementById('responsavelProjetoEdicao').value = responsavel ? responsavel : "";
    document.getElementById('clienteEdicaoProjeto').value = clienteEdicaoProjeto;
    document.getElementById('dataInicioEdicao').value = dataInicio;
    document.getElementById('dataFimEdicao').value = previsaoFim;

    document.getElementById('iniciadoEdicao').addEventListener("change", function () {
        if (this.checked) {
            document.getElementById('dataInicioEdicao').disabled = false;
            document.getElementById('dataInicioEdicao').value = hoje;
            document.getElementById('text-data-inicio-edicao').hidden = true;
        } else {
            document.getElementById('dataInicioEdicao').disabled = true;
            document.getElementById('dataInicioEdicao').value = "";
            document.getElementById('text-data-inicio-edicao').hidden = false;
        }
    });

    if (iniciado === "true") {
        document.getElementById('iniciadoEdicao').checked = true;
        document.getElementById('dataInicioEdicao').disabled = false;
        document.getElementById('text-data-inicio-edicao').style.display = 'none';
    } else {
        document.getElementById('iniciadoEdicao').checked = false;
        document.getElementById('dataInicioEdicao').disabled = true;
        document.getElementById('dataInicioEdicao').val("");
        document.getElementById('text-data-inicio-edicao').style.display = 'block';
    }
}

function salvarEdicaoProjeto() {
    var iniciadoProjeto = document.getElementById('iniciadoEdicao').checked;
    var idProjeto = document.getElementById('idProjetoEdicao').value;
    var nome = document.getElementById('nomeProjetoEdicao').value;
    var descricao = document.getElementById('descricaoProjetoEdicao').value;
    var status = document.getElementById('statusEdicaoProjeto').value;
    var prioridade = document.getElementById('prioridadeEdicao').value;
    var responsavel = document.getElementById('responsavelProjetoEdicao').value;
    var clienteEdicaoProjeto = document.getElementById('clienteEdicaoProjeto').value;
    var dataInicio = document.getElementById('dataInicioEdicao').value;
    var previsaoFim = document.getElementById('dataFimEdicao').value;

    if (!nome || !descricao || !status || !prioridade || !responsavel || !clienteEdicaoProjeto || !previsaoFim) {
        Swal.fire({
            title: "Ops!",
            text: "Preencha todos os campos!",
            icon: "warning",
            confirmButtonText: 'OK'
        })
        return;
    }

    $.ajax({
        url: '/projetos/editar',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            idProjeto: idProjeto,
            nomeProjeto: nome,
            descricaoProjeto: descricao,
            statusProjeto: status,
            prioridadeProjeto: prioridade,
            responsavelProjeto: responsavel,
            clienteProjeto: clienteEdicaoProjeto,
            dataInicioProjeto: dataInicio,
            previsaoFimProjeto: previsaoFim,
            iniciadoProjeto: iniciadoProjeto,
        }),
        success: function() {
            Swal.fire({
                title: "Sucesso!",
                text: "Projeto editado com sucesso.",
                icon: "success",
                confirmButtonText: "OK"
            }).then(() => {
                location.reload();
            });
        },
        error: function(xhr, status, error) {
            Swal.fire({
                title: "Ops!",
                text: "Ocorreu um erro ao editar o projeto.",
                icon: "error",
                confirmButtonText: "OK"
            });
        }
    });
}
