function validarSenha(senha) {
    var regexSenha = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    return regexSenha.test(senha);
}

function exibirMensagemErro(elemento, mensagem) {
    elemento.textContent = mensagem;
    elemento.classList.remove('mensagem-escondida');
    elemento.classList.add('mensagem-visivel');

    setTimeout(function () {
        elemento.classList.remove('mensagem-visivel');
        elemento.classList.add('mensagem-escondida');
    }, 6500);
}

function logar() {
    var email = document.getElementById('email').value;
    var password = document.getElementById('password').value;

    if (!email || !password) {
        Swal.fire({
            title: "Ops!",
            text: "Preencha todos os campos!",
            icon: "warning",
            confirmButtonText: 'OK'
        });
        return;
    }

    $.ajax({
        url: '/logar',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ email: email, password: password }),
        success: function() {
            window.location.href = "/home/dashboard";
        },
        error: function(xhr, status, error) {
            Swal.fire({
                title: "Ops!",
                text: "Email ou senha inválidos.",
                icon: "error",
                confirmButtonText: "OK"
            });
        }
    });
}


function cadastrar() {
    var nomeCadastro = document.getElementById('nomeCadastro').value;
    var sobrenomeCadastro = document.getElementById('sobrenomeCadastro').value;
    var emailCadastro = document.getElementById('emailCadastro').value;
    var senhaCadastro = document.getElementById('passwordCadastro').value;
    var organizacaoCadastro = document.getElementById('organizacaoCadastro').value;

    if (!nomeCadastro || !sobrenomeCadastro || !emailCadastro || !senhaCadastro) {
        Swal.fire({
            title: "Ops!",
            text: "Preencha todos os campos!",
            icon: "warning",
            confirmButtonText: 'OK'
        })
        return;
    }

    if(!validarSenha(senhaCadastro)) {
        exibirMensagemErro(mensagemErroSenha, 'A senha deve ter no mínimo 8 caracteres, incluindo maiúsculas, minúsculas, números e caracteres especiais.');
        return;
    }

    $.ajax({
        url: '/register',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            nome: nomeCadastro,
            sobrenome: sobrenomeCadastro,
            email: emailCadastro,
            password: senhaCadastro,
            organizacao: organizacaoCadastro,
            role: "USER",
        }),
        complete: function(xhr, status) {
            switch (xhr.status) {
                case 200:
                    Swal.fire({
                        title: "Pronto!",
                        text: "Cadastrado com sucesso!",
                        icon: "success",
                        confirmButtonText: 'OK'
                    }).then(() => {
                        window.location.href = "/login";
                    });
                    break;
                case 400:
                    Swal.fire({
                        title: "Ops!",
                        text: "Email já cadastrado.",
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