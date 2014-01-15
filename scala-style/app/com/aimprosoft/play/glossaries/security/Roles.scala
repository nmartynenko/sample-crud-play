package com.aimprosoft.play.glossaries.security

import be.objectify.deadbolt.core.models.Role

class SimpleRole(name: String) extends Role {
  def getName: String = name

  override def toString: String = getName
}

object UserRole extends SimpleRole("user")

object AdminRole extends SimpleRole("admin")

object GlossaryRoles {
  private lazy val userRoles = List(UserRole)

  private lazy val adminRoles = List(UserRole, AdminRole)

  def apply(role: String) = role.toLowerCase match {
    case "user" => userRoles
    case "admin" => adminRoles
    case _ => throw new IllegalArgumentException(s"No role found for role $role")
  }
}