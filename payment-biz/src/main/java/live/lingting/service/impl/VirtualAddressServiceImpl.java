package live.lingting.service.impl;

import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import live.lingting.Page;
import live.lingting.dto.VirtualAddressCreateDTO;
import live.lingting.entity.Project;
import live.lingting.entity.VirtualAddress;
import live.lingting.mapper.VirtualAddressMapper;
import live.lingting.sdk.model.MixVirtualPayModel;
import live.lingting.service.VirtualAddressService;
import live.lingting.virtual.VirtualHandler;

/**
 * @author lingting 2021/6/7 15:43
 */
@Service
@RequiredArgsConstructor
public class VirtualAddressServiceImpl extends ExtendServiceImpl<VirtualAddressMapper, VirtualAddress>
		implements VirtualAddressService {

	private static final Integer SHUFFLE_MIN = 3;

	private final VirtualHandler handler;

	@Override
	public VirtualAddress lock(MixVirtualPayModel model, Project project) {
		final List<VirtualAddress> list = baseMapper.load(model.getChain(), project.getId(), project.getMode());
		// 乱序
		if (list.size() > SHUFFLE_MIN) {
			Collections.shuffle(list);
		}
		for (VirtualAddress va : list) {
			// 锁
			if (baseMapper.lock(va)) {
				va.setUsing(true);
				return va;
			}
		}
		return null;
	}

	@Override
	public boolean unlock(String address) {
		return baseMapper.unlock(address);
	}

	@Override
	public PageResult<VirtualAddress> list(Page<VirtualAddress> page, VirtualAddress qo) {
		return baseMapper.list(page, qo);
	}

	@Override
	public void disabled(Integer id, Boolean disabled) {
		baseMapper.disabled(id, disabled);
	}

	@Override
	public VirtualAddressCreateDTO create(VirtualAddressCreateDTO dto) {
		for (VirtualAddressCreateDTO.Va address : dto.getList()) {
			address.setSuccess(false);
			final VirtualAddress va = new VirtualAddress().setAddress(address.getAddress()).setChain(dto.getChain())
					.setMode(dto.getMode()).setProjectIds(dto.getIds());
			if (!handler.valid(va)) {
				address.setDesc("无效地址!");
				continue;
			}

			if (baseMapper.selectCount(baseMapper.getWrapper(va)) > 0) {
				address.setDesc("已存在相同地址!");
			}

			try {
				va.setDisabled(dto.getDisabled());
				address.setSuccess(save(va));
				if (!address.getSuccess()) {
					address.setDesc("保存失败!");
				}
			}
			catch (Exception e) {
				address.setSuccess(false);
				address.setDesc("保存异常!");
			}
		}

		return dto;
	}

}
