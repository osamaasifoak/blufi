import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.Socket
import java.io.InputStream

class ChargerSocketManager(private val host: String, private val port: Int) {

    suspend fun sendCommand(command: ByteArray): Boolean {
        var socket: Socket? = null
        return try {
            withContext(Dispatchers.IO) {
                socket = Socket(host, port)
                socket!!.getOutputStream().write(command)
                socket!!.getOutputStream().flush()
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            socket?.close()
        }
    }

    suspend fun getSystemVersions(): Boolean {
        var socket: Socket? = null
        return try {
            withContext(Dispatchers.IO) {
                socket = Socket(host, port)

                val reqHeaderSize = 3
                val resHeaderSize = 7
                val resDataSize = 15
                val resSize = resHeaderSize + resDataSize
                val netcomsReq = ByteArray(reqHeaderSize)

                // Set up the request
                val commandValue = 0x1002
                netcomsReq[0] = (commandValue and 0xFF).toByte()
                netcomsReq[1] = ((commandValue shr 8) and 0xFF).toByte()
                netcomsReq[2] = reqHeaderSize.toByte()

                // Send the request
                socket!!.getOutputStream().write(netcomsReq)
                socket!!.getOutputStream().flush()
                println(netcomsReq)
                // Receive the response
                val buffer = readBytes(socket!!.getInputStream(), resSize)

                // Handle the response
                val resData = buffer.copyOfRange(resHeaderSize, resSize)
                val firmwareVersion = "${resData[2]}.${resData[3]}.${resData[4]}"
                val apiVersion = "${resData[0]}.${resData[1]}"

                println("API Version: ${apiVersion}")
                println("Firmware Version: ${firmwareVersion}")
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            socket?.close()
        }
    }

  private  suspend fun readBytes(inputStream: InputStream, size: Int): ByteArray {
        return try{
            withContext(Dispatchers.IO) {
                val result = mutableListOf<Byte>()
                val buffer = ByteArray(1024)

                while (result.size < size) {
                    val bytesRead = inputStream.read(buffer)
                    if (bytesRead == -1) break
                    result.addAll(buffer.take(bytesRead))
                }

                result.toByteArray()
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            throw (e)
        }
    }
}
