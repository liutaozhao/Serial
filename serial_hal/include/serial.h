
#include <hardware/hardware.h>
#include <fcntl.h>
#include <errno.h>
#include <cutils/log.h>
#include <cutils/atomic.h>

#define serial_HARDWARE_MODULE_ID "serial"


/*ÿһ��Ӳ��ģ�鶼ÿ������һ����ΪHAL_MODULE_INFO_SYM�����ݽṹ������
���ĵ�һ����Ա�����ͱ���Ϊhw_module_t*/
struct serial_module_t {
	struct hw_module_t common; //ģ������
	
}; /*��hardware.h�е�hw_module_t�����˵����
xxx_module_t�ĵ�һ����Ա������hw_module_t���ͣ�
��β���ģ���һ�������Ϣ����ȻҲ���Բ����壬
�����û�ж���ģ�������Ϣ
*/



/*ÿһ���豸���ݽṹ�ĵ�һ����Ա����������hw_device_t���ͣ�
��β��Ǹ�����������������*/

struct serial_control_device_t {
	struct hw_device_t common; //�豸����
	/* supporting control APIs go here */

	int (*serial_read_hal)(struct serial_control_device_t *dev, char *buf, int count);
	/***************************************/
	int (*serial_write_hal)(struct serial_control_device_t *dev, char *buf, int count);
	/***************************************/
};
/*��hardware.h�е�hw_device_t��˵����
Ҫ���Զ���xxx_device_t�ĵ�һ����Ա������hw_device_t���ͣ�
��β���������һЩ�ӿ���Ϣ
*/
