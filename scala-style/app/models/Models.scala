package models

case class Glossary(id: Long = 0, name: String, description: String)

case class User(id: Long = 0, email: String, password: String, name: String, role: String)