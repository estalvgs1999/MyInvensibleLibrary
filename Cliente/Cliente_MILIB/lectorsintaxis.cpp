#include "lectorsintaxis.h"
using namespace std;

LectorSintaxis::LectorSintaxis(string inputIDE){
    this->inputIDE = inputIDE;
    this->inputSize = inputIDE.size();
    this->idError = 0;
}

string LectorSintaxis::manejarInputIDE(){
    string instruccion = obtenerInstruccion();
    if(instruccion == "INSERT")
        instruccion = manejarInstruccionInsert();
    else if(instruccion == "SELECT")
        instruccion = manejarInstruccionSelect();
    else if(instruccion == "DELETE")
        instruccion = manejarInstruccionDelete();
    else if(instruccion == "UPDATE")
        instruccion = manejarInstruccionUpdate();

    qDebug()<<instruccion.c_str();
    return instruccion;
}

string LectorSintaxis::obtenerInstruccion(){
    string instruccion;
    string caracterActual;
    for(int posicion = 0; posicion < inputSize; posicion++){
        caracterActual = inputIDE[posicion];
        if(caracterActual != " ")
            instruccion += caracterActual;
        else{
            inputIDE = inputIDE.substr(posicion+1, inputSize-posicion);
            inputSize = inputIDE.size();
            return instruccion;
        }
    }
    idError = 1;
    return "ERROR";
}

string LectorSintaxis::manejarInstruccionInsert(){
    string verificarSintaxis = inputIDE.substr(0, 13);
    if(verificarSintaxis != "INTO METADATA"){
        idError = 2;
        return "ERROR";
    }
    inputIDE = inputIDE.substr(13, inputIDE.size()-13);
    inputSize = inputIDE.size();
    string columnas = obtenerColumnasInsert();
    string valores = obtenerValoresInsert();
    return columnas + "-" + valores;
}

string LectorSintaxis::obtenerContenidoTupla(){
    string contenido;
    string caracterActual;
    for(int posicion = 0; posicion < inputSize; posicion++){
        caracterActual = inputIDE[posicion];
        if(caracterActual != " "){
            if(caracterActual != ")")
                contenido += caracterActual;
            else{
                inputIDE = inputIDE.substr(posicion+1, inputSize-posicion);
                inputSize = inputIDE.size();
                return contenido;
            }
        }
    }
    return "ERROR";
}


string LectorSintaxis::obtenerColumnasInsert(){
    string caracterActual;
    for(int posicion = 0; posicion < inputSize; posicion++){
        caracterActual = inputIDE[posicion];
        if(caracterActual != " "){
            if(caracterActual == "("){
                inputIDE = inputIDE.substr(posicion+1, inputSize-posicion);
                inputSize = inputIDE.size();
                break;
            }else{
                idError = 3;
                return "ERROR";
            }
        }
    }

    string nombreColumnas = obtenerContenidoTupla();
    if(nombreColumnas == "ERROR")
        idError = 4;
    return nombreColumnas;
}

string LectorSintaxis::obtenerValoresInsert(){
    string caracterActual;
    string verificarSintaxis;
    for(int posicion = 0; posicion < inputSize; posicion++){
        caracterActual = inputIDE[posicion];
        if(caracterActual != " " && caracterActual != "\n"){
            if(caracterActual == "("){
                inputIDE = inputIDE.substr(posicion+1, inputSize-posicion);
                inputSize = inputIDE.size();
                break;
            }else
                verificarSintaxis += caracterActual;
        }
    }

    if(verificarSintaxis != "VALUES"){
        idError = 5;
        return "ERROR";
    }

    string valores = obtenerContenidoTupla();
    string finInstruccion;
    finInstruccion = inputIDE[0];
    if(valores == "ERROR")
        idError = 6;
    else if(finInstruccion != ";"){
        idError = 7;
        valores = "ERROR";
    }
    return valores;
}

string LectorSintaxis::manejarInstruccionSelect(){
    string columnas = obtenerColumnasSelect();
    if(idError == 0){
        if(inputIDE.substr(0, 13) != "FROM METADATA"){
            idError = 9;
            return "ERROR";
        }
        inputIDE = inputIDE.substr(13, inputSize-13);
        inputSize = inputIDE.size();
        if(inputIDE == ";")
            return columnas;
        else if(inputIDE.substr(1, 6) == "WHERE "){
            string condicional = obtenerCondicionalSelect();
            return columnas + "-" + condicional;
        }else{
            idError = 10;
            return "ERROR";
        }
    }return "ERROR";
}

string LectorSintaxis::obtenerColumnasSelect(){
    string columnas;
    string caracterActual;
    for(int posicion = 0; posicion < inputSize; posicion++){
        caracterActual = inputIDE[posicion];
        if(caracterActual != " ")
            columnas += caracterActual;
        else if(columnas.substr(posicion-1, 1) != ","){
            inputIDE = inputIDE.substr(posicion+1, inputSize-posicion);
            inputSize = inputIDE.size();
            return columnas;
        }
    }
    idError = 8;
    return "ERROR";
}

string LectorSintaxis::obtenerCondicionalSelect(){
    inputIDE = inputIDE.substr(7, inputSize-7);
    inputSize = inputIDE.size();
    if(inputIDE.substr(inputSize-1, 1) != ";"){
        idError = 7;
        return "ERROR";
    }
    return inputIDE;
}

string LectorSintaxis::manejarInstruccionDelete(){
    if(inputIDE.substr(0, 20) != "FROM METADATA WHERE "){
        idError = 11;
        return "ERROR";
    }
    inputIDE = inputIDE.substr(20, inputSize-20);
    inputSize = inputIDE.size();
    if(inputIDE.substr(inputSize-1, 1) != ";"){
        idError = 7;
        return "ERROR";
    }
    return inputIDE;
}

string LectorSintaxis::manejarInstruccionUpdate(){
    if(inputIDE.substr(0, 13) != "METADATA\nSET "){
        idError = 12;
        return "ERROR";
    }
    inputIDE = inputIDE.substr(13, inputSize-13);
    inputSize = inputIDE.size();
    string caracterActual;
    string setStr;
    for(int posicion = 0; posicion < inputSize; posicion++){
        caracterActual = inputIDE[posicion];
        if(caracterActual != "\n")
            setStr += caracterActual;
        else{
            inputIDE = inputIDE.substr(posicion+1, inputSize-posicion);
            inputSize = inputIDE.size();
            break;
        }
    }

    if(inputIDE.substr(0, 6) != "WHERE "){
        idError = 13;
        return "ERROR";
    }

    inputIDE = inputIDE.substr(6, inputSize-6);
    inputSize = inputIDE.size();


    return setStr + "-" + inputIDE;
}

//INSERT INTO METADATA (NOMBRE, ARTISTA, DURACION, ALBUM)
//VALUES ("Karma Police", "Radiohead", "4:27", "OK Computer");

//SELECT NOMBRE, ALBUM FROM METADATA
//WHERE ejemplo = "valor";

//DELETE FROM METADATA WHERE ejemplo = "valor";

//UPDATE METADATA
//SET ejemplo1 = "valor1", ejemplo2 = "valor2"
//WHERE ejemplo3 = "valor3";