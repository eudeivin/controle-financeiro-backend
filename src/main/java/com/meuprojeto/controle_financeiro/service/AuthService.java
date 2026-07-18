package com.meuprojeto.controle_financeiro.service;

import com.meuprojeto.controle_financeiro.dto.AuthResponseDTO;
import com.meuprojeto.controle_financeiro.dto.LoginRequestDTO;
import com.meuprojeto.controle_financeiro.dto.RegistroRequestDTO;
import com.meuprojeto.controle_financeiro.entity.Usuario;
import com.meuprojeto.controle_financeiro.exception.RegraNegocioException;
import com.meuprojeto.controle_financeiro.repository.UsuarioRepository;
import com.meuprojeto.controle_financeiro.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponseDTO registrar(RegistroRequestDTO dto) {
        if (usuarioRepository.findByEmail(dto.email()).isPresent()) {
            throw new RegraNegocioException("Já existe um usuário cadastrado com esse email");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setSenha(passwordEncoder.encode(dto.senha())); // senha criptografada!

        usuario = usuarioRepository.save(usuario);

        String token = jwtUtil.gerarToken(usuario);

        return new AuthResponseDTO(token, usuario.getId(), usuario.getNome(), usuario.getEmail());
    }

    public AuthResponseDTO login(LoginRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.senha())
        );

        Usuario usuario = usuarioRepository.findByEmail(dto.email())
                .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado"));

        String token = jwtUtil.gerarToken(usuario);

        return new AuthResponseDTO(token, usuario.getId(), usuario.getNome(), usuario.getEmail());
    }
}