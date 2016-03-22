
#include <hardware/hardware.h>
#include <fcntl.h>
#include <errno.h>
#include <cutils/log.h>
#include <cutils/atomic.h>

#define serial_HARDWARE_MODULE_ID "serial"


/*每一个硬件模块都每必须有一个名为HAL_MODULE_INFO_SYM的数据结构变量，
它的第一个成员的类型必须为hw_module_t*/
struct serial_module_t {
	struct hw_module_t common; //模块类型
	
}; /*见hardware.h中的hw_module_t定义的说明，
xxx_module_t的第一个成员必须是hw_module_t类型，
其次才是模块的一此相关信息，当然也可以不定义，
这里就没有定义模块相关信息
*/



/*每一个设备数据结构的第一个成员函数必须是hw_device_t类型，
其次才是各个公共方法和属性*/

struct serial_control_device_t {
	struct hw_device_t common; //设备类型
	/* supporting control APIs go here */

	int (*serial_read_hal)(struct serial_control_device_t *dev, char *buf, int count);
	/***************************************/
	int (*serial_write_hal)(struct serial_control_device_t *dev, char *buf, int count);
	/***************************************/
};
/*见hardware.h中的hw_device_t的说明，
要求自定义xxx_device_t的第一个成员必须是hw_device_t类型，
其次才是其它的一些接口信息
*/
