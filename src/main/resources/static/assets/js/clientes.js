$(document).ready(function () {
    paginacaoTabela('tabelaClientes')
});

document.getElementById("nomeClienteBusca").addEventListener("keyup", filtrarTabela);

function filtrarTabela() {
    var nomeFilter = document.getElementById("nomeClienteBusca").value.toLowerCase();

    var table = document.querySelector(".table");
    var rows = table.querySelectorAll("tbody tr");

    rows.forEach(function(row) {
        var nome = row.querySelector("td:nth-child(1)").textContent.toLowerCase();
        var nomeMatch = nome.indexOf(nomeFilter) > -1 || nomeFilter === "";

        if (nomeMatch) {
            row.style.display = "";
        } else {
            row.style.display = "none";
        }
    });
}

function criarCliente() {
    var nomeCliente = document.getElementById('nomeCliente').value;
    var descricaoCliente = document.getElementById('descricaoCliente').value;

    if (!nomeCliente) {
        Swal.fire({
            title: "Ops!",
            text: "Preencha pelo menos o campo de nome!",
            icon: "warning",
            confirmButtonText: 'OK'
        });
        return;
    }

    $.ajax({
        url: '/clientes/criar',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            nomeCliente: nomeCliente,
            descricaoCliente: descricaoCliente
        }),
        success: function() {
            Swal.fire({
                title: "Sucesso!",
                text: "Cliente criado com sucesso.",
                icon: "success",
                confirmButtonText: "OK"
            }).then(() => {
                window.location.href = "/clientes";
            });
        },
        error: function(xhr, status, error) {
            Swal.fire({
                title: "Ops!",
                text: "Ocorreu um erro ao criar o cliente.",
                icon: "error",
                confirmButtonText: "OK"
            });
        }
    });
}

function excluirCliente(button) {
    var idCliente = button.getAttribute('data-id');
    Swal.fire({
        icon: 'info',
        title: 'Deseja excluir este cliente?',
        showDenyButton: true,
        confirmButtonText: 'Sim',
        denyButtonText: 'Não',
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: '/clientes/excluir',
                type: 'POST',
                data: {
                    idCliente: idCliente
                },
                complete: function(xhr, status) {
                    switch (xhr.status) {
                        case 200:
                            Swal.fire({
                                title: "Pronto!",
                                text: "Excluído com sucesso!",
                                icon: "success",
                                confirmButtonText: 'OK'
                            }).then(() => {
                                location.reload();
                            });
                            break;
                        case 304:
                            Swal.fire({
                                title: "Ops!",
                                text: "Não foi possível excluir este cliente pois o mesmo possui projetos atribuídos à ele.",
                                icon: "warning",
                                confirmButtonText: 'OK'
                            })
                            break;
                        default:
                            alert("Erro desconhecido: " + status);
                    }
                }
            });
        }
    })
}

function modalEditarCliente(button) {
    var idCliente = button.getAttribute('data-id');
    var nomeCliente = button.getAttribute('data-nome');
    var descricaoCliente = button.getAttribute('data-descricao');
    $('#modalEditarCliente').modal('show');
    $('#idClienteEdicao').val(idCliente);
    $('#nomeClienteEdicao').val(nomeCliente);
    $('#descricaoClienteEdicao').val(descricaoCliente);
}

function salvarEdicaoCliente() {
    var idClienteEdicao = document.getElementById('idClienteEdicao').value;
    var nomeClienteEdicao = document.getElementById('nomeClienteEdicao').value;
    var descricaoClienteEdicao = document.getElementById('descricaoClienteEdicao').value;

    if (!nomeClienteEdicao) {
        Swal.fire({
            title: "Ops!",
            text: "Preencha pelo menos o campo de nome!",
            icon: "warning",
            confirmButtonText: 'OK'
        });
        return;
    }

    $.ajax({
        url: '/clientes/editar',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            idClienteEdicao: idClienteEdicao,
            nomeClienteEdicao: nomeClienteEdicao,
            descricaoClienteEdicao: descricaoClienteEdicao
        }),
        success: function() {
            Swal.fire({
                title: "Sucesso!",
                text: "Cliente editado com sucesso.",
                icon: "success",
                confirmButtonText: "OK"
            }).then(() => {
                location.reload();
            });
        },
        error: function(xhr, status, error) {
            Swal.fire({
                title: "Ops!",
                text: "Ocorreu um erro ao editar o cliente.",
                icon: "error",
                confirmButtonText: "OK"
            });
        }
    });
}