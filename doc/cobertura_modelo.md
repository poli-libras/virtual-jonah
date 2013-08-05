
Cobertura do modelo
=====================

Esta é uma check list baseada nas propriedades da [modelagem dos sinais](http://code.google.com/p/sign-model), onde a ideia é que Jonah possa representar todos os aspectos do modelo (símbolo, mão, face etc).

Symbol 
------------
* **Contact**: não suportado
* **Hands in Unity**: OK
* **Locations**: suportado parcialmente

Face
-----

Não suportado

Hand
------

* **Orientation**: suportado
* **Plane**: suportado
* **Rotation**: suportado
* **Shape**: suportado parcialmente
* **Fingers movement**: não 

Hand Movement
----------------

* **Type + segments**: suportado parcialmente
* **Speed**: suportado
* **Frequency**: suportado
* **Starts in location**: com bug


Locations
---------

As coordenadas das locações estão *hard-coded* na classe LocationsLoader. Seria melhor que as coordenadas fossem carregadas de arquivos, que seriam gerados pelo designer do modelo.

As locações destacadas já estão definidas.

 * **ESPACO_NEUTRO**

Grupo cabeça:
 * **TOPO_DA_CABECA**
 * **TESTA**
 * ROSTO
 * ROSTO_SUPERIOR
 * ROSTO_INFERIOR
 * ORELHA
 * **OLHOS**
 * **NARIZ**
 * **BOCA**
 * BOCHECHAS
 * QUEIXO

Grupo corpo:
 * PESCOCO
 * OMBRO
 * **BUSTO**
 * ESTOMAGO
 * CINTURA
 * BRACOS
 * ANTEBRACO
 * COTOVELO
 * PULSO

Grupo mão:
 * PALMA
 * COSTAS_DA_MAO
 * LADO_INDICADOR
 * LADO_DEDO_MINIMO
 * DEDOS
 * PONTA_DOS_DEDOS
 * DEDO_MINIMO
 * ANULAR
 * DEDO_MEDIO
 * INDICADOR
 * POLEGAR

Para as locações da cabeça, basta adicionar as coordenadas das locações ainda não definidas: o Jonah saberá como utilizar as coordenadas ainda não definidas, o algoritmo para tal já está implementado.

Para o corpo, nós podemos definir algumas locações (como *busto*), mas outras locações (como *ombros*) não fazem sentido em um Jonah sem corpo.

O grupo mais complicado é o da mão: a interação entre duas mão é bem complexa e o software (os algoritmos dos movimentos) deve ser melhorado para que isso seja possível.

== Configurações ==

Para que o Jonah tenha suporte a novas configurações de mão, "basta" fazer novos modelos OBJ e adicionar estes arquivos no diretório apropriado (junto dos outros arquivos OBJ).

Confira as configurações já modeladas: https://github.com/poli-libras/virtual-jonah/tree/master/resources/models/dir.

Antes de fazer um modelo novo, considere a frequência das configurações de mão na LIBRAS: https://raw.github.com/poli-libras/virtual-jonah/master/doc/freq_conf.xls

== Movimento da mão ==

O único tipo de movimento suportado é o RETILINIO.

Há ainda os seguintes a serem implementados: CURVO, HELICOIDAL, SINUOSO e CIRCULAR.

