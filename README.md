# Sistema de Controle de Estoque

Este sistema foi desenvolvido como parte de um projeto acadêmico, com o objetivo de auxiliar pequenas empresas e escritórios na **gestão de estoque**, controle de **movimentações de produtos** e permissões de usuários com diferentes níveis de acesso.

## 🎯 Objetivo

Oferecer uma ferramenta simples e funcional que permita:
- Cadastrar, editar, remover e visualizar produtos do estoque
- Controlar retiradas e visualizar o histórico de movimentações
- Gerenciar usuários e aplicar restrições de acesso por departamento

---

## 🛠️ Tecnologias Utilizadas

- **Java 17+**
- **Maven** (para gerenciamento de dependências e build)
- **Swing + MigLayout** (para interface gráfica)
- **FlatLaf** (tema moderno para Swing)
- **SQLite** (banco de dados local embutido)

---

## 👤 Usuários para Teste

O sistema já possui usuários padrão com diferentes permissões de acesso:

| Usuário   | Senha     | Departamento | Acessos Permitidos                                              |
|-----------|-----------|--------------|------------------------------------------------------------------|
| `admin`   | `admin`   | SUPERVISAO   | Acesso total: estoque, usuários, movimentação e histórico       |
| `usuarios`| `usuarios`| GERAL        | Apenas login, sem acesso a nenhuma funcionalidade sensível      |
| `financeiro` | `financeiro` | FINANCEIRO | Estoque, movimentações e histórico de retiradas                |

---

## 🚀 Como Executar

### Requisitos:
- Java Runtime Environment 17 ou superior instalado

### Passo a passo:
1. Extraia a pasta do sistema para qualquer local
2. Dê dois cliques no arquivo `controle-estoque.jar`
   - ou abra o terminal e execute:
     ```bash
     java -jar controle-estoque.jar
     ```


## 📌 Observações

- O sistema é executado localmente e **não requer internet**
- A base de dados é um arquivo `.db`, o que facilita o transporte
- Todas as permissões e validações são feitas conforme o login do usuário

---

## 👨‍💻 Desenvolvido por
**Murilo Aparecido de Oliveira**  
RA: 2301222 – Curso de Análise e Desenvolvimento de Sistemas