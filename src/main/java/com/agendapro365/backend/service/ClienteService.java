package com.agendapro365.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.agendapro365.backend.dto.ClienteDTO;
import com.agendapro365.backend.exception.ResourceNotFoundException;
import com.agendapro365.backend.model.Cliente;
import com.agendapro365.backend.model.Usuario;
import com.agendapro365.backend.repository.ClienteRepository;
import com.agendapro365.backend.repository.UsuarioRepository;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;

    //Inyeccion por constructor
    public ClienteService(ClienteRepository clienteRepository, UsuarioRepository usuarioRepository) {
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    //Crear un nuevo cliente
    public ClienteDTO crearCliente(ClienteDTO dto) {
        Cliente cliente = toEntity(dto);
        Cliente guardado = clienteRepository.save(cliente);
        return toDTO(guardado);
    }

    //obtener cliente por ID
    public ClienteDTO obtenerClientePorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
              .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
        
        return toDTO(cliente);
    }

    //Listar todos los clientes
    public List<ClienteDTO> listarClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        //api stream de java 8
        return clientes.stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Actualizar cliente
    public ClienteDTO actualizarClienteDTO(Long id, ClienteDTO dto) {
       Cliente clienteExistente = clienteRepository.findById(id)
              .orElseThrow(()-> new ResourceNotFoundException("Cliente no existe"));

              clienteExistente.setNombre(dto.getNombre());
              clienteExistente.setEmail(dto.getEmail());
              clienteExistente.setTelefono(dto.getTelefono());

              if (dto.getUsuarioId() != null ) {
                Usuario usuario = usuarioRepository.findById(dto.getId())
                       .orElseThrow(()-> new ResourceNotFoundException("Usuario no existe"));
                       clienteExistente.setUsuario(usuario);
              }

              Cliente actualizado = clienteRepository.save(clienteExistente);
            return toDTO(actualizado);
    }

    // Eliminar cliente
    public void eliminarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
              .orElseThrow(()-> new ResourceNotFoundException("Cliente no encontrado"));

              clienteRepository.delete(cliente);
    }

    private ClienteDTO toDTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setNombre(cliente.getNombre());
        dto.setEmail(cliente.getEmail());
        dto.setTelefono(cliente.getTelefono());
        if (cliente.getUsuario() != null) {
            dto.setUsuarioId(cliente.getUsuario().getId());
        }

        return dto;
    }

    //Conversion DTO -> Entity
    private Cliente toEntity(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefono(dto.getTelefono());

        if (dto.getUsuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no existe"));
                cliente.setUsuario(usuario);
        }
        return cliente;
    }

}
