# Cadastrator

Aplicativo para cadastro de endereços.

Utiliza banco de dados nativo SQLite, acessado por meio da biblioteca Room. Faz a verificação do CEP por meio da API https://viacep.com.br/, utilizando Retrofit2 para fazer as chamadas.
O aplicativo usa o conceito de Single Activity (utiliza uma unica activity), fazendo o fluxo do usuario por meio de fragments.
