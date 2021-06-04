package live.lingting.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**项目
 * @author lingting 2021/6/4 0:42
 */
@Getter
@Setter
@TableName("project")
public class Project {

	@TableId
	private Integer id;

	/**
	 * 项目名
	 */
	@Size(max = 50, message = "项目名最多使用50个字符!")
	private String name;

	/**
	 * 是否禁用
	 */
	private Boolean disabled;

	private String apiKey;

	private String apiSecurity;

	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

}
