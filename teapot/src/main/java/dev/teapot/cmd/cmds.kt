package dev.teapot.cmd

import dev.teapot.msg.Msg
import kotlin.reflect.KClass

open class AbstractCmd
open class Cmd : AbstractCmd()
open class SwitchCmd : Cmd()
data class CancelCmd(val cancelCmd: Cmd) : Cmd()
data class CancelByClassCmd<T : Cmd>(val cmdClass: KClass<T>) : Cmd()
object None : Cmd()
data class ProxyCmd(val msg: Msg) : Cmd()
open class ViewCmd : Cmd()

data class BatchCmd(val cmds: MutableSet<Cmd>) : Cmd() {
    constructor(vararg commands: Cmd) : this(commands.toMutableSet())

    fun merge(cmd: Cmd): BatchCmd {
        when (cmd) {
            is BatchCmd -> {
                cmds.addAll(cmd.cmds)
            }
            is None -> {}
            else -> {
                cmds.add(cmd)
            }
        }
        return this
    }
}