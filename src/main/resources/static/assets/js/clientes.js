$(document).ready(function () {
    paginacaoTabela('tabelaClientes')
});

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