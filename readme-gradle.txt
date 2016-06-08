-Conforme entendido a publicação do sign-model será, em primeiro momento, feita em diretório local na máquina do desenvolvedor.
-Decidi utilizar o Gradle Wrapper para que não seja necessária a instalação do Gradle na máquina do desenvolvedor.


Abaixo uma breve explicação para executar os builds (caso esteja utilizando o Linux substituir o comando "gradlew" por "./gradlew"):

1. Build do sign-model:
	-no diretório raiz do projeto sign-model execute "gradlew build"

2. Publicação do sign-model em repositório/diretório local. Obs: está configurado para publicar dentro da pasta 'repositorioFlat' que será criada no diretório raiz do projeto sign-model:
	-no diretório raiz do projeto sign-model execute "gradlew uploadArchives"

3. Build do virtual-jonah (depende da publicação descrita no item anterior):
	-edite a variável signModelProjectLocalDir dentro do arquivo build.gradle para que aponte para o diretório raiz local do projeto sign-model
	-no diretório raiz do projeto virtual-jonah execute "gradlew build"

4. Criei também uma task que cria um Jar do vitual-jonah com todas as dependências mas não sei se é necessário.
	-no diretório raiz do projeto virtual-jonah execute "gradlew fatJar"
    # Léo: não achei onde o JAR ficou!

Para trabalhar os Projetos Gradle no Eclipse é necessário instalar o plugin "Buildship: Eclipse Plug-ins for Gradle". Daí é só importar os projetos como "Gradle Project".
