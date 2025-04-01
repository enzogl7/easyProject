function criarResponsavel() {
    var nomeResponsavel = document.getElementById('nomeResponsavel').value;

    if (!nomeResponsavel) {
        Swal.fire({
            title: "Ops!",
            text: "Preencha o campo de nome!",
            icon: "warning",
            confirmButtonText: 'OK'
        });
        return;
    }

    $.ajax({
        url: '/responsaveis/criar',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            nomeResponsavel: nomeResponsavel
        }),
        success: function() {
            Swal.fire({
                title: "Sucesso!",
                text: "Responsável criado com sucesso.",
                icon: "success",
                confirmButtonText: "OK"
            }).then(() => {
                window.location.href = "/responsaveis";
            });
        },
        error: function(xhr, status, error) {
            Swal.fire({
                title: "Ops!",
                text: "Ocorreu um erro ao criar o responsável.",
                icon: "error",
                confirmButtonText: "OK"
            });
        }
    });
}

function modalEditarResponsavel(button) {
    var idResponsavel = button.getAttribute('data-id');
    var nomeResponsavel = button.getAttribute('data-nome-responsavel')
    $('#modalEditarResponsavel').modal('show');
    $('#idResponsavelEdicao').val(idResponsavel);
    $('#nomeResponsavelEdicao').val(nomeResponsavel);
}

function salvarEdicaoResponsavel() {
    var idResponsavelEdicao = document.getElementById('idResponsavelEdicao').value;
    var nomeResponsavelEdicao = document.getElementById('nomeResponsavelEdicao').value;

    if (!nomeResponsavelEdicao) {
        Swal.fire({
            title: "Ops!",
            text: "Preencha o campo de nome.",
            icon: "warning",
            confirmButtonText: 'OK'
        });
        return;
    }

    $.ajax({
        url: '/responsaveis/editar',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            idResponsavelEdicao: idResponsavelEdicao,
            nomeResponsavelEdicao: nomeResponsavelEdicao
        }),
        success: function() {
            Swal.fire({
                title: "Sucesso!",
                text: "Responsável editado com sucesso.",
                icon: "success",
                confirmButtonText: "OK"
            }).then(() => {
                location.reload();
            });
        },
        error: function(xhr, status, error) {
            Swal.fire({
                title: "Ops!",
                text: "Ocorreu um erro ao editar o responsável.",
                icon: "error",
                confirmButtonText: "OK"
            });
        }
    });
}

function excluirResponsavel(button) {
    var idResponsavel = button.getAttribute('data-id');
    Swal.fire({
        icon: 'info',
        title: 'Deseja excluir este responsável?',
        showDenyButton: true,
        confirmButtonText: 'Sim',
        denyButtonText: 'Não',
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: '/responsaveis/excluir',
                type: 'POST',
                data: {
                    idResponsavel: idResponsavel
                },
                success: function() {
                    Swal.fire({
                        title: "Sucesso!",
                        text: "Responsável excluído com sucesso.",
                        icon: "success",
                        confirmButtonText: "OK"
                    }).then(() => {
                        location.reload();
                    });
                },
                error: function(xhr, status, error) {
                    Swal.fire({
                        title: "Ops!",
                        text: "Ocorreu um erro ao excluir responsável.",
                        icon: "error",
                        confirmButtonText: "OK"
                    });
                }
            });
        }
    })
}