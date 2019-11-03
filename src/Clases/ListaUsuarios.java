package Clases;

import java.util.HashMap;

public class ListaUsuarios {
    private HashMap<Long,Usuarios> compitas;
    private HashMap<Long,Usuarios> personas;

    public ListaUsuarios(HashMap<Long, Usuarios> compitas, HashMap<Long, Usuarios> personas) {
        this.compitas = compitas;
        this.personas = personas;
    }

    public HashMap<Long, Usuarios> getCompitas() {
        return compitas;
    }

    public void setCompitas(HashMap<Long, Usuarios> compitas) {
        this.compitas = compitas;
    }

    public HashMap<Long, Usuarios> getPersonas() {
        return personas;
    }

    public void setPersonas(HashMap<Long, Usuarios> personas) {
        this.personas = personas;
    }
}
