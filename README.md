Virtual Jonah
=================================

Virtual Jonah lê uma descrição em XML de uma sequência de sinais para que um avatar animado em 3D sintetize os movimentos no espaço virtual para representar estes sinais descritos no XML.

A modelagem dos sinais (estrutura do XML) é determinada pelo projeto sign-model.

Tecnologias
-----------

A API gráfica utilizada é o Processing (uma API em Java que por baixo utiliza o OpenGL).

Os modelos gráficos foram feitos no 3DStudioMAX e exportados no formato texto OBJ, que é lido pela aplicação. 

Comandos
--------

* x: carrega sinais de resources/xml/signs.xml
* c: carrega todas as configurações de mão
* y: executa sinais 
* Espaço: executa próximo símbolo
* a, d, w, s, q, e: controle de câmera

Comandos devem ser executados com foco no applet.

Captura de tela
---------------

![Captura de tela do Virtual Jonah](https://raw.github.com/poli-libras/virtual-jonah/master/doc/jonah-small.png)


English description
-------------------

Virtual Jonah software reads a XML description of a sign sequence and the avatar synthesizes the movements to represent such signs.

The sign modeling is defined on the sign-model project. 

