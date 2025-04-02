function criarSolicitante() {
    var nomeSolicitante = document.getElementById('nomeSolicitante').value;

    if (!nomeSolicitante) {
        Swal.fire({
            title: "Ops!",
            text: "Preencha o campo de nome",
            icon: "warning",
            confirmButtonText: 'OK'
        });
        return;
    }

    $.ajax({
        url: '/solicitantes/criar',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            nomeSolicitante: nomeSolicitante
        }),
        success: function() {
            Swal.fire({
                title: "Sucesso!",
                text: "Solicitante criado com sucesso.",
                icon: "success",
                confirmButtonText: "OK"
            }).then(() => {
                window.location.href = "/solicitantes";
            });
        },
        error: function(xhr, status, error) {
            Swal.fire({
                title: "Ops!",
                text: "Ocorreu um erro ao criar o solicitante.",
                icon: "error",
                confirmButtonText: "OK"
            });
        }
    });
}

function modalEditarSolicitante(button) {
    var idSolicitante = button.getAttribute('data-id');
    var nomeSolicitante = button.getAttribute('data-nome-solicitante')
    $('#modalEditarSolicitante').modal('show');
    $('#idSolicitanteEdicao').val(idSolicitante);
    $('#nomeSolicitanteEdicao').val(nomeSolicitante);
}

function salvarEdicaoSolicitante() {
    var idSolicitanteEdicao = document.getElementById('idSolicitanteEdicao').value;
    var nomeSolicitanteEdicao = document.getElementById('nomeSolicitanteEdicao').value;

    if (!nomeSolicitanteEdicao) {
        Swal.fire({
            title: "Ops!",
            text: "Preencha o campo de nome.",
            icon: "warning",
            confirmButtonText: 'OK'
        });
        return;
    }

    $.ajax({
        url: '/solicitantes/editar',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            idSolicitanteEdicao: idSolicitanteEdicao,
            nomeSolicitanteEdicao: nomeSolicitanteEdicao
        }),
        success: function() {
            Swal.fire({
                title: "Sucesso!",
                text: "Solicitante editado com sucesso.",
                icon: "success",
                confirmButtonText: "OK"
            }).then(() => {
                location.reload();
            });
        },
        error: function(xhr, status, error) {
            Swal.fire({
                title: "Ops!",
                text: "Ocorreu um erro ao editar o solicitante.",
                icon: "error",
                confirmButtonText: "OK"
            });
        }
    });

}

function excluirSolicitante(button) {
    var idSolicitante = button.getAttribute('data-id');

    Swal.fire({
        icon: 'info',
        title: 'Deseja excluir este solicitante?',
        showDenyButton: true,
        confirmButtonText: 'Sim',
        denyButtonText: 'Não',
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: '/solicitantes/excluir',
                type: 'POST',
                data: {
                    idSolicitante: idSolicitante
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
                                text: "Não foi possível excluir este solicitante pois o mesmo possui projetos atribuídos à ele.",
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